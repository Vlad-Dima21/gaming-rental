import { Skeleton } from "@/components/ui/skeleton";
import { Suspense } from "react";
import RentalsPageComponent from "./RentalsPageComponent";

export interface SearchParams {
	clientId: string;
	deviceName: string;
	returned: string;
    pastDue: string;
    page: string;
	sort: string;
}

function LoadingSkeleton() {
	return (
		<div className='p-4 sm:p-8 md:p-16 flex flex-col items-center'>
			<div className='space-y-20 max-w-5xl w-full'>
                <div className='flex flex-col lg:flex-row justify-between items-center gap-5'>
                    <Skeleton className='h-10 w-[20%]' />
                    <Skeleton className='h-10 w-[45%]' />
                </div>
                <div className='w-full flex flex-col gap-5'>
                    <Skeleton className='h-[80px]' />
                    <Skeleton className='h-[80px]' />
                    <Skeleton className='h-[80px]' />
                    <Skeleton className='h-[80px]' />
                </div>
			</div>
		</div>
	);
}

export default async function RentalsPage({
	searchParams: awaitableSearchParams,
}: {
	searchParams: Promise<Partial<SearchParams>>;
}) {
	const searchParams = await awaitableSearchParams;
	return (
		<Suspense fallback={<LoadingSkeleton />}>
			<RentalsPageComponent searchParams={searchParams} />
		</Suspense>
	);
}