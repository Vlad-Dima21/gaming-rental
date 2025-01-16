'use client'

import React, {useState, useTransition} from 'react';
import {z} from 'zod';
import {clientValidation} from '@/lib/zod';
import {Input} from "@/components/ui/input";
import {get, post} from '@/actions'
import {APIResponse, EntityException} from "@/types";
import {Client} from "@/types";
import {cn} from "@/lib/utils";
import {Label} from "@/components/ui/label";
import {Button} from "@/components/ui/button";
import {Check, UserRoundPlus} from "lucide-react";
import { Loader } from '@/components/Loader';

const ClientComponent: React.FC = () => {
    const [clientInfoError, setClientInfoError] = useState<string | null>(null);
    const [clientInfo, setClientInfo] = useState<Client | null>(null);
    const [clientInfoIsPending, startClientInfoTransition] = useTransition();
    const [newClient, setNewClient] = useState<Omit<Client, 'clientId'>>({
        clientName: '',
        clientEmail: '',
        clientPhone: ''
    });
    const [createClientIsPending, startCreateClientTransition] = useTransition();
    const [serverClient, setServerClient] = useState<Client | null>(null);
    const [errors, setErrors] = useState<{ [key: string]: string }>({});

    const handleClientIdSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setClientInfoError(null);
        setClientInfo(null);
        const formElements = e.currentTarget.elements as typeof e.currentTarget.elements & {
            clientId: HTMLInputElement;
        };
        startClientInfoTransition(async () => {
            try {
                const response = await get(`/clients/${formElements.clientId.value}`).then(res => JSON.parse(res) as APIResponse<Client>);
                if (response.ok) {
                    setClientInfo(response.body as Client);
                } else if (response.status == 404) {
                    setClientInfoError('Client not found');
                } else {
                    setClientInfoError((response.body as EntityException).message);
                }
            } catch (err) {
                console.error(err);
            }
        })
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setNewClient({...newClient, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (serverClient) {
            setServerClient(null);
            return;
        }
        try {
            clientValidation.parse(newClient);
            startCreateClientTransition(async () => {
                const response = await post('/clients/create', newClient).then(res => JSON.parse(res) as APIResponse<Client>);
                if (response.ok) {
                    setServerClient(response.body as Client);
                    setNewClient({clientName: '', clientEmail: '', clientPhone: ''});
                    setErrors({});
                } else {
                    const exception = response.body as EntityException;
                    if (exception.fieldName) {
                        setErrors({[exception.fieldName]: exception.details ?? exception.message});
                    } else {
                        setErrors({internalError: exception.message});
                    }
                }
            });
        } catch (error) {
            if (error instanceof z.ZodError) {
                const fieldErrors: { [key: string]: string } = {};
                error.errors.forEach(err => {
                    if (err.path.length > 0) {
                        fieldErrors[err.path[0]] = err.message;
                    }
                });
                setErrors(fieldErrors);
            }
        }
    };

    return (
        <main className="p-4 sm:p-8 md:p-16 flex flex-col items-center bg-background">
            <div className='flex justify-between max-w-5xl w-full'>
            <div className='pe-4 flex flex-col space-y-3 w-1/2'>
                <form className='space-y-2' onSubmit={handleClientIdSubmit}>
                    <Label className='text-2xl font-bold' htmlFor="clientId">Search for an existing client</Label>
                    <div className='relative'>
                        <Input
                            id={"clientId"}
                            name="clientId"
                            required
                            type="number"
                            placeholder="Enter Client ID"
                            className="border ps-2 pe-4 w-full [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                        />
                        <Loader
                            className={cn('absolute right-2 top-[.6rem]', clientInfoIsPending ? 'inline' : 'hidden')}/>
                    </div>
                    <input type='submit' hidden/>
                    {clientInfoError && <p className="text-destructive">{clientInfoError}</p>}
                </form>
                {clientInfo && (
                    <div className="mt-4">
                        <p className='text-foreground'><strong>Name:</strong> {clientInfo.clientName}</p>
                        <p className='text-foreground'><strong>Email:</strong> {clientInfo.clientEmail}</p>
                        <p className='text-foreground'><strong>Phone:</strong> {clientInfo.clientPhone}</p>
                    </div>
                )}
            </div>
            <div className="w-1/2 ps-4 border-l">
                <form className='space-y-6' onSubmit={handleSubmit}>
                    <Label className='text-2xl font-bold' htmlFor="clientName">Add a new client</Label>
                    <div className='!mt-2'>
                        <Input
                            id={"clientName"}
                            type="text"
                            name="clientName"
                            placeholder={'Client Name'}
                            value={newClient.clientName}
                            onChange={handleInputChange}
                            className="border p-2 w-full"
                            disabled={!!serverClient}
                        />
                        {errors.clientName && <p className="text-destructive">{errors.clientName}</p>}
                    </div>
                    <div>
                        <Input
                            type="email"
                            name="clientEmail"
                            placeholder={'Client Email'}
                            value={newClient.clientEmail}
                            onChange={handleInputChange}
                            className="border p-2 w-full"
                            disabled={!!serverClient}
                        />
                        {errors.clientEmail && <p className="text-destructive">{errors.clientEmail}</p>}
                    </div>
                    <div>
                        <Input
                            type="text"
                            name="clientPhone"
                            placeholder={'Client Phone'}
                            value={newClient.clientPhone}
                            onChange={handleInputChange}
                            className="border p-2 w-full"
                            disabled={!!serverClient}
                        />
                        {errors.clientPhone && <p className="text-destructive">{errors.clientPhone}</p>}
                    </div>
                    {!createClientIsPending ?
                        <Button type="submit" className={serverClient ? 'bg-green-600 hover:bg-green-600' : ''}
                                effect='expandIcon'
                                icon={!serverClient ? UserRoundPlus : Check} iconPlacement='right'>
                            {serverClient ? (
                                <span>The ID for the new client is <span
                                    className='font-bold'>{serverClient.clientId}</span></span>
                            ) : (
                                <span>Add client</span>
                            )}
                        </Button> : <Button disabled={true} variant={"outline"} className={'flex items-center'}>
                            <span>Adding client</span>
                            <Loader className="inline"/>
                        </Button>}
                    {errors.internalError && <p className="text-destructive">{errors.internalError}</p>}
                </form>
            </div>
            </div>
        </main>
    );
};

export default ClientComponent;