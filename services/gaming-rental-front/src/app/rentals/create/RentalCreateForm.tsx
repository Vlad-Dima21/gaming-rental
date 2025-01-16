'use client';

import { Button } from '@/components/ui/button';
import { Label } from '@radix-ui/react-label';
import { Plus } from 'lucide-react';
import {
	useActionState,
	useEffect,
	useState,
	useTransition,
} from 'react';
import { createRental } from './actions';
import { Input } from '@/components/ui/input';
import Device, { APIResponse, DeviceBase } from '@/types';
import DeviceCard from './DeviceCard';
import { get } from '@/actions';
import { Loader } from '@/components/Loader';
import { Skeleton } from '@/components/ui/skeleton';
import { Slider } from '@/components/ui/slider';

export default function RentalCreateForm({
	devices,
}: {
	devices: DeviceBase[];
}) {
	const [errorMessage, formAction, createRentalPending] = useActionState(
		createRental,
		null
    );
    
    const [clientId, setClientId] = useState<string>();

	const [selectedDeviceBaseId, setSelectedDeviceBaseId] = useState<
		number | undefined
	>(devices[0]?.deviceBaseId);
	const [getUnitsPending, startFetchUnits] = useTransition();
	const [deviceUnits, setDeviceUnits] = useState<Device[] | null>(null);
	useEffect(() => {
		startFetchUnits(async () => {
			if (selectedDeviceBaseId) {
				const response = await get(
					`/units/of-id/${selectedDeviceBaseId}`
				).then((res) => JSON.parse(res) as APIResponse<Device[]>);
				if (response.ok) {
					const data = response.body as Device[];
					for (const device of data) {
						await get(`/rentals/is-unit-available/${device.deviceId}`).then((res) => {
							const data = JSON.parse(
								res
							) as APIResponse<boolean>;
							if (data.ok) {
								device.deviceIsAvailable = data.body as boolean;
							} else {
								device.deviceIsAvailable = false;
							}
						});
					}
					setDeviceUnits(data);
				} else {
					setDeviceUnits(null);
				}
			}
		});
	}, [selectedDeviceBaseId]);

    const [selectedDevice, setSelectedDevice] = useState<Device | null>(null);
    const [noOfDays, setNoOfDays] = useState(30);

	return (
		<main className='p-4 sm:p-8 md:p-16 bg-background'>
			<div className='max-w-5xl w-full m-auto'>
				<form className='space-y-6' action={formAction}>
					<Label className='text-2xl font-bold' htmlFor='clientId'>
						Create a new rental
					</Label>
					<div className='!mt-2'>
						<Input
                            id={'clientId'}
                            required
							type='number'
							name='clientId'
							placeholder={'Client ID'}
							className='border p-2 w-full [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none'
                            disabled={!!createRentalPending}
                            value={clientId ?? ''}
                            onChange={(event) => setClientId(event.target.value)}
						/>
					</div>
					<Button asChild variant={'outline'}>
						<select
							disabled={createRentalPending}
							onChange={(event) =>
								setSelectedDeviceBaseId(
									parseInt(event.target.value)
								)
							}
						>
							{devices.map((device) => (
								<option
									key={device.deviceBaseId}
									value={device.deviceBaseId}
								>
									{device.deviceBaseName}
								</option>
							))}
						</select>
					</Button>
					<div className='overflow-auto'>
						<div className='flex items-center gap-4 pb-2'>
							{!getUnitsPending &&
								(deviceUnits?.length ? (
									deviceUnits.map((device, idx) => (
										<DeviceCard
											key={device.deviceId}
											index={idx}
											device={device}
											className='w-fit'
											isDeviceInCart={
												device == selectedDevice
											}
											toggleDevice={(device) =>
												setSelectedDevice(
													device == selectedDevice
														? null
														: device
												)
											}
                                            toggleEnabled={!createRentalPending}
										/>
									))
								) : (
									<span className='text-lg font-bold'>
										No units available. Please select
										another device
									</span>
								))}
							{getUnitsPending && (
								<div className='flex gap-2 pb-2'>
									<Skeleton className='w-[180px] h-[150px]' />
									<Skeleton className='w-[180px] h-[150px]' />
									<Skeleton className='w-[180px] h-[150px]' />
								</div>
							)}
						</div>
                    </div>
                    <input hidden name='deviceId' readOnly value={selectedDevice?.deviceId ?? ''}/>
                    <div className='flex items-center gap-4 pb-2'>
                        <span className='shrink-0 line-clamp-1'>Number of days: <span className='font-bold'>{noOfDays}</span></span>
                        <Slider
                            className='shrink-2'
                        defaultValue={[30]}
                        min={30}
                        max={90}
                        value={[noOfDays]}
                        onValueChange={(value) =>
                            setNoOfDays(value[0])
                        }
                            disabled={createRentalPending}
                    />
                    </div>
                    <input hidden name='numberOfDays' readOnly value={noOfDays}/>
					<Button
						type='submit'
						effect={!createRentalPending ? 'expandIcon' : null}
						icon={!createRentalPending ? Plus : Loader}
                        iconPlacement='right'
                        className='w-full'
					>
						Create rental
					</Button>
					<p className='text-destructive'>{errorMessage}</p>
				</form>
			</div>
		</main>
	);
}
