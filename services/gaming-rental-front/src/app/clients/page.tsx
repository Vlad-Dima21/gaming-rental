import ClientPageComponent from "@/app/clients/ClientPageComponent";
import {Metadata} from "next";

export const metadata: Metadata = {
    title: 'Clients',
    description: 'Manage clients',
}

export default function Page() {
    return <ClientPageComponent />;
}