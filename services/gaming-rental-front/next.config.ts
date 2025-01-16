import type { NextConfig } from "next";

const hostnames = [
    'cdn.ozone.ro',
    'www.google.com',
    'lcdn.altex.ro',
    's13emagst.akamaized.net',
    'gmedia.playstation.com',
    'www.punchtechnology.co.uk',
    'm.media-amazon.com',
];

const nextConfig: NextConfig = {
  /* config options here */
    output: 'standalone',
    images: {
        remotePatterns: hostnames.map((hn) => ({
            protocol: 'https',
            hostname: hn,
        })),
    }
};

export default nextConfig;
