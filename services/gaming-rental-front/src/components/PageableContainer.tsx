import { ReadonlyURLSearchParams, redirect } from 'next/navigation';
import { Button } from './ui/button';
import Link from 'next/link';
import { ChevronLeft, ChevronRight } from 'lucide-react';

export default function PageableContainer({
	searchParams,
	totalPages,
	baseUrl,
	children,
	noResultsText
}: {
	searchParams: { [key: string]: string };
	totalPages: number;
	baseUrl: string;
		children: React.ReactNode;
		noResultsText?: string;
}) {
	const page = searchParams.page ? parseInt(searchParams.page) : 1;

	if (page > totalPages && totalPages > 0) {
		redirect(
			`${baseUrl}?${new ReadonlyURLSearchParams({
				...searchParams,
				page: totalPages.toString(),
			}).toString()}`
		);
	}
	return (
		<div className='w-full h-full flex flex-col gap-3 justify-between'>
			{totalPages > 0 && children}
			{totalPages == 0 && (
				<div className='flex flex-col justify-end items-center min-h-[200px]'>
					<span className='text-lg font-bold'>
						{noResultsText ?? 'No results match your search'}
					</span>
				</div>
			)}
			{totalPages > 0 && (
				<div className='flex w-full justify-between'>
					<Button
						asChild
						variant='outline'
            className={page == 1 ? 'invisible' : ''}
            effect='expandIcon'
            iconPlacement='left'
            icon={ChevronLeft}
					>
						<Link
							href={`${baseUrl}?${new ReadonlyURLSearchParams({
								...searchParams,
								page: (page - 1).toString(),
							})}`}
						>
							Previous page
						</Link>
					</Button>
					<Button
						asChild
						className={page == totalPages ? 'invisible' : ''}
						effect='expandIcon'
						iconPlacement='right'
						icon={ChevronRight}
					>
						<Link
							href={`${baseUrl}?${new ReadonlyURLSearchParams({
								...searchParams,
								page: (page + 1).toString(),
							})}`}
						>
							Next page
						</Link>
					</Button>
				</div>
			)}
		</div>
	);
}
