import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"
import {ReadonlyURLSearchParams} from "next/navigation";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export default function strippedUrlSearchParams(
    formData: FormData
): ReadonlyURLSearchParams {
  const formParams = Object.fromEntries(formData.entries());
  return new ReadonlyURLSearchParams(
      Object.fromEntries(
          Object.entries(formParams).filter(([, v]) => !!v)
      ) as Record<string, string>
  );
}
