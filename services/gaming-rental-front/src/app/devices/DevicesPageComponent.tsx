import {ReadonlyURLSearchParams, redirect} from "next/navigation";
import {get} from '@/actions';
import strippedUrlSearchParams from "@/lib/utils";
import SortButton from "@/components/SortButton";
import {Input} from "@/components/ui/input";
import PageableContainer from "@/components/PageableContainer";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import Image from "next/image";
import {APIResponse, DeviceBase, PageableResponse} from "@/types";
import { SearchParams } from "./page";


export default async function DevicesPageComponent({
    searchParams,
}: {
    searchParams: Partial<SearchParams>;
    }) {
	const request = await get(
        `/devices?${new ReadonlyURLSearchParams(searchParams).toString()}`
    ).then(res => JSON.parse(res) as APIResponse<PageableResponse<DeviceBase>>);
    if (!request.ok) {
        console.error(request.body);
        return <h1 className="text-3xl m-5 font-bold text-center">Ooops. Something went wrong</h1>;
    }
    const data = request.body as PageableResponse<DeviceBase>;
    const { items: devices, totalPages } = data;

    return (
        <main className='p-4 sm:p-8 md:p-16 flex flex-col items-center'>
            <div className='space-y-5 max-w-5xl w-full'>
                <div className='flex flex-col lg:flex-row justify-between items-center gap-5'>
                    <h1 className='text-2xl font-bold'>Devices</h1>
                    <form
                        className='flex gap-2 flex-col lg:flex-row w-full lg:w-auto max-w-5xl'
                        action={async (formData: FormData) => {
                            'use server';
                            redirect(`/devices/?${strippedUrlSearchParams(formData).toString()}`);
                        }}
                    >
                        <div className='flex gap-2 w-full lg:w-auto'>
                            <SortButton searchParams={searchParams} baseUrl='/devices'>
                                Name
                            </SortButton>
                            <Input
                                className='flex-grow lg:flex-initial md:w-[230px]'
                                placeholder='Search by name...'
                                name='name'
                                defaultValue={searchParams.name ?? ''}
                            />
                        </div>
                        <Input
                            className='w-full'
                            placeholder='Search by producer...'
                            name='producer'
                            defaultValue={searchParams.producer ?? ''}
                        />
                        <input type='submit' hidden />
                    </form>
                </div>
                <PageableContainer
                    searchParams={searchParams}
                    totalPages={totalPages}
                    baseUrl='/devices'
                >
                    <div className='w-full flex flex-col gap-5'>
                        {devices.map((device) => (
                            <div
                                key={device.deviceBaseName}
                                // href={`/device/${device.deviceBaseId}`}
                            >
                                <Card className='flex flex-col md:flex-row gap-2 p-5 justify-between bg-white/70 backdrop-blur-sm hover:border-primary hover:shadow-md'>
                                    <div>
                                        <CardHeader>
                                            <CardTitle>{device.deviceBaseName}</CardTitle>
                                        </CardHeader>
                                        <CardContent>
                                            <p>{device.deviceBaseProducer}</p>
                                            <p>{device.deviceBaseYearOfRelease}</p>
                                        </CardContent>
                                    </div>
                                    <Image
                                        src={device.deviceBaseImageUrl}
                                        width={150}
                                        height={150}
                                        objectFit='cover'
                                        className='border border-gray-300 rounded-md self-center md:self-auto'
                                        alt={device.deviceBaseName}
                                    />
                                </Card>
                            </div>
                        ))}
                        {devices.length === 0 && (
                            <p className='self-center mt-20'>No devices found</p>
                        )}
                    </div>
                </PageableContainer>
            </div>
        </main>
    );
}
