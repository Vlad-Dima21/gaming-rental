import React from 'react';
import { Button } from './ui/button';
import Link from 'next/link';
import { ReadonlyURLSearchParams } from 'next/navigation';
import {
  ArrowDownWideNarrow,
  ArrowUpWideNarrow,
} from 'lucide-react';

interface SearchParamsWithSort {
  page: string | undefined;
  sort: string | undefined;
}

export const SortDirection = {
  Ascending: 'asc',
  Descending: 'desc',

  opposite(value?: string): string {
    switch (value) {
      case 'desc':
        return SortDirection.Ascending;
      default:
        return SortDirection.Descending;
    }
  },
};

export default function SortButton({
  children,
  searchParams,
  baseUrl,
}: {
  children: React.ReactNode;
  searchParams: Partial<SearchParamsWithSort>;
  baseUrl: string;
}) {
  const sort = SortDirection.opposite(searchParams.sort),
    ArrowComp =
      sort == SortDirection.Ascending ? ArrowDownWideNarrow : ArrowUpWideNarrow;
  const strippedSearchParams = { ...searchParams };
  delete strippedSearchParams.page;
  return (
    <>
      <input type='hidden' name='sort' readOnly value={searchParams.sort ?? SortDirection.Ascending} />
      <Button asChild variant={'outline'} className='gap-2'>
        <Link
          href={`${baseUrl}?${new ReadonlyURLSearchParams({
            ...strippedSearchParams,
            sort: sort,
          }).toString()}`}
        >
          {children}
          <ArrowComp size={20} />
        </Link>
      </Button>
    </>
  );
}
