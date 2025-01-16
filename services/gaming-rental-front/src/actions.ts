'use server'

import {APIResponse} from "@/types";

const apiUrl = process.env.API_URL!;

async function commonFetch<T>(
    url: string,
    method: string,
    data?: object
): Promise<string> {
    try {
        return await fetch(`${apiUrl}${url}`, {
            method,
            headers: {
                'Content-Type': 'application/json',
            },
            ...(data && { body: JSON.stringify(data) }),
        }).then(async resp => {
            try {
                const json: T = await resp.json();
                console.log(url, json);
                return JSON.stringify(new APIResponse(resp.ok, resp.status, json));
            } catch (error: unknown) {
                console.log(`Internal API error on ${url} with status ${resp.status}: ${(error as Error).message}`);
                return JSON.stringify(new APIResponse(false, resp.status, 'Internal API error'));
            }
        });
    } catch (error) {
        console.error(`Network error on ${url}: ${(error as Error).message}`);
        return JSON.stringify(new APIResponse(false, 500, 'Network error'));
    }
}

export async function get(url: string, data?: object): Promise<string> {
    return commonFetch(url, 'GET', data);
}

export async function post(url: string, data?: object): Promise<string> {
    return commonFetch(url, 'POST', data);
}

export async function patch(url: string, data?: object): Promise<string> {
    return commonFetch(url, 'PATCH', data);
}

export async function del(url: string, data?: object): Promise<string> {
    return commonFetch(url, 'DELETE', data);
}