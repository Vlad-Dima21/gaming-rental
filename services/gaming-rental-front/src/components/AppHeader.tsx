import React from 'react';
import Link from 'next/link';
import {Button} from "@/components/ui/button";
import {Gamepad2} from "lucide-react";

const AppHeader: React.FC = () => {
    return (
        <header className="bg-primary p-4">
            <nav className="flex space-x-20 justify-between max-w-5xl m-auto">
                <Link href={'/'} className='flex gap-2 items-center text-secondary'>
                    <Gamepad2 size={32} />
                    <span className='text-2xl font-bold'>Gaming Rental</span>
                </Link>
                <div>
                    <Button variant="link" effect="hoverUnderline" asChild>
                        <Link href="/clients" className="text-secondary">
                            Clients
                        </Link>
                    </Button>
                    <Button variant="link" effect="hoverUnderline" asChild>
                        <Link href="/devices" className="text-secondary">
                            Devices
                        </Link>
                    </Button>
                    <Button variant="link" effect="hoverUnderline" asChild>
                        <Link href="/rentals" className="text-secondary">
                            Rentals
                        </Link>
                    </Button>
                </div>
            </nav>
        </header>
    );
};

export default AppHeader;