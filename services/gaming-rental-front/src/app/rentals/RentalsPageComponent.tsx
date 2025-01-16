import PageableContainer from '@/components/PageableContainer';
import { SearchParams } from './page';
import { ReadonlyURLSearchParams, redirect } from 'next/navigation';
import strippedUrlSearchParams from '@/lib/utils';
import { get, patch } from '@/actions';
import Device, {
	APIResponse,
	DeviceBase,
	PageableResponse,
	Rental,
} from '@/types';
import SortButton from '@/components/SortButton';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { revalidatePath } from 'next/cache';
import { Button } from '@/components/ui/button';
import Link from 'next/link';
import { Plus } from 'lucide-react';

export default async function RentalsPageComponent({
	searchParams,
}: {
	searchParams: Partial<SearchParams>;
}) {
	const rentalRequest = searchParams.clientId
		? await get(
				`/rentals?${new ReadonlyURLSearchParams(
					searchParams
				).toString()}`
		  ).then(
				(res) =>
					JSON.parse(res) as APIResponse<PageableResponse<Rental>>
		  )
		: null;
	if (rentalRequest && !rentalRequest?.ok) {
		console.error(rentalRequest.body);
		return (
			<h1 className='text-3xl m-5 font-bold text-center'>
				Ooops. Something went wrong
			</h1>
		);
	}

	const data = (rentalRequest?.body as PageableResponse<Rental>) ?? {
		items: [],
		totalPages: 0,
	};
	const { items: rentals, totalPages } = data;

	const devicesBase = new Map();
	await Promise.all(
		rentals.map(async (rental) => {
			const deviceRequest = await get(
				`/units/${rental.rentalDeviceId}`
			).then((res) => JSON.parse(res) as APIResponse<Device>);
			if (deviceRequest.ok) {
				rental.rentalDevice = deviceRequest.body as Device;
				rental.rentalDevice.deviceName =
					devicesBase.get(rental.rentalDevice.deviceBaseId) ??
					(await get(
						`/devices/${rental.rentalDevice.deviceBaseId}`
					).then((res) => {
						const response = JSON.parse(
							res
						) as APIResponse<DeviceBase>;
						if (response.ok) {
							const result = (response.body as DeviceBase)
								.deviceBaseName;
							devicesBase.set(
								rental.rentalDevice.deviceBaseId,
								result
							);
							return result;
						}
					}));
			}
		})
	);

	return (
		<main className='p-4 sm:p-8 md:p-16 flex flex-col items-center'>
			<div className='space-y-5 max-w-5xl w-full'>
				<div className='flex flex-col lg:flex-row justify-between items-center gap-5'>
					<h1 className='text-2xl font-bold'>Rentals</h1>
					<form
						className='flex gap-2 flex-col lg:flex-row w-full lg:w-auto max-w-5xl'
						action={async (formData: FormData) => {
							'use server';
							redirect(
								`/rentals/?${strippedUrlSearchParams(
									formData
								).toString()}`
							);
						}}
					>
						<Button
							asChild
							effect='expandIcon'
							icon={Plus}
							iconPlacement='right'
						>
							<Link href={'/rentals/create'}>Create Rental</Link>
						</Button>
						<div className='flex gap-2 w-full lg:w-auto'>
							<SortButton
								searchParams={searchParams}
								baseUrl='/rentals'
							>
								Return date
							</SortButton>
							<Input
								className='flex-grow lg:flex-initial md:w-[230px]'
								placeholder='Client ID'
								name='clientId'
								defaultValue={searchParams.clientId ?? ''}
							/>
						</div>
						<input type='submit' hidden />
					</form>
				</div>
				<PageableContainer
					searchParams={searchParams}
					totalPages={totalPages}
					baseUrl='/rentals'
					noResultsText={
						searchParams.clientId?.length
							? 'No rentals found'
							: 'Please enter a client ID'
					}
				>
					<div className='w-full flex flex-col gap-5'>
						{rentals
							.map((rental) => ({
								...rental,
								isPastDue:
									new Date(rental.rentalDueDate) <
									(rental.rentalReturnDate
										? new Date(rental.rentalReturnDate)
										: new Date()),
								isReturned: !!rental.rentalReturnDate,
							}))
							.map((rental) => (
								<Card
									key={new Date(
										rental.rentalDueDate
									).getTime()}
									className='flex flex-col md:flex-row gap-2 p-5 justify-between bg-white/70 backdrop-blur-sm hover:shadow-md'
								>
									<div className='w-full'>
										<CardHeader>
											<CardTitle className='flex flex-col gap-2'>
												<div className='flex items-end gap-2'>
													<span>
														{
															rental.rentalDevice
																.deviceName
														}
													</span>
													<span className='text-sm text-gray-600'>
														due{' '}
														{new Date(
															rental.rentalDueDate
														).toLocaleDateString()}
													</span>
												</div>
												<Badge
													variant={
														rental.isReturned
															? 'default'
															: 'outline'
													}
													className={`w-fit space-x-2 ${
														rental.isPastDue
															? 'bg-red-500 text-white'
															: rental.isReturned
															? 'bg-green-400 text-white'
															: ''
													}`}
												>
													<span>
														{rental.isReturned
															? 'Returned'
															: 'On rent'}
													</span>
													{rental.rentalReturnDate && (
														<span>
															{new Date(
																rental.rentalReturnDate
															).toLocaleDateString()}
														</span>
													)}
												</Badge>
											</CardTitle>
										</CardHeader>
										<CardContent className='w-full flex flex-col md:flex-row justify-end gap-5'>
											{!rental.isReturned && (
												<form
													action={async (
														formData: FormData
													) => {
														'use server';
														const rentalId: number =
															parseInt(
																formData.get(
																	'rentalId'
																) as string
															);
														await patch(
															`/rentals/return/${rentalId}`
														);
														revalidatePath(
															'/rentals'
														);
													}}
													className='flex-shrink md:self-end'
												>
													<input
														hidden
														name='rentalId'
														readOnly
														value={
															rental.rentalId ??
															''
														}
													/>
													<Button
														variant={
															rental.isReturned
																? 'outline'
																: 'default'
														}
														className='w-full'
													>
														Return
													</Button>
												</form>
											)}
										</CardContent>
									</div>
								</Card>
							))}
					</div>
				</PageableContainer>
			</div>
		</main>
	);
}
