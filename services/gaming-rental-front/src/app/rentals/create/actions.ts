'use server';

import { post } from '@/actions';
import { APIResponse, EntityException, Rental } from '@/types';
import { redirect } from 'next/navigation';

export const createRental = async (_previousState: string | null, formData: FormData) => {
	const clientId = formData.get('clientId') as string;
	const deviceUnitId = formData.get('deviceId') as string;
	const numberOfDays = formData.get('numberOfDays') as string;

		const response = await post('/rentals/create', {
			clientId,
			deviceUnitId,
			numberOfDays,
		}).then((res) => JSON.parse(res) as APIResponse<Rental>);
		console.log(response);
		if (response.ok) {
			redirect(`/rentals?clientId=${clientId}`);
		}
		return (
			(response.body as EntityException).details ??
			(response.body as EntityException).message
		);
};
