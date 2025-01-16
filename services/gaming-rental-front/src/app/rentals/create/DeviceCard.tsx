'use client';

import { HTMLAttributes } from 'react';
import { Gamepad } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';
import Device from '@/types';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';

interface DeviceCardProps extends HTMLAttributes<HTMLDivElement> {
  device: Device;
  index: number;
  isDeviceInCart: boolean;
  toggleDevice: (device: Device) => void;
  toggleEnabled: boolean;
}

export default function DeviceCard({
  device,
  index,
  isDeviceInCart,
  toggleDevice,
  toggleEnabled,
  ...props
}: DeviceCardProps) {

  return (
    <Card
      {...props}
      className={cn(
        'flex flex-col gap-2 p-5 justify-around bg-white/70 backdrop-blur-sm hover:shadow-md min-w-[200px] transition-all duration-200',
        props.className
      )}
    >
      <div className='flex items-center gap-2'>
        <span>Unit no. {index + 1}</span>
        <Badge variant={device.deviceIsAvailable ? 'default' : 'destructive'}>
          {device.deviceIsAvailable ? 'Available' : 'Not available'}
        </Badge>
      </div>
      <div className='flex items-center gap-2'>
        <Gamepad />
        <span>
          {device.deviceNumberOfControllers}{' '}
          {device.deviceNumberOfControllers > 1 ? 'controllers' : 'controller'}
        </span>
      </div>
      <Button
        type='button'
        variant={isDeviceInCart ? 'destructive' : 'default'}
          onClick={() => toggleDevice(device)}
          disabled={!toggleEnabled || !device.deviceIsAvailable}
          className='mt-6'
        >
          {!isDeviceInCart ? 'Rent' : 'Remove from rental'}
        </Button>
    </Card>
  );
}
