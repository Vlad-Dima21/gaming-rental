import { APIResponse, DeviceBase } from '@/types';
import RentalCreateForm from './RentalCreateForm';
import { get } from '@/actions';

export default async function RentalCreatePage() {
    const response = await get('/devices/all').then((res) => JSON.parse(res) as APIResponse<DeviceBase[]>);

    if (!response.ok) {
        return <h1 className='text-3xl m-5 font-bold text-center'>Ooops. Something went wrong</h1>;
    }
    
    return <RentalCreateForm devices={response.body as DeviceBase[]} />;
}