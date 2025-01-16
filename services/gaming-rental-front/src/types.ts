export interface Client {
	clientId: number;
	clientName: string;
	clientEmail: string;
	clientPhone: string;
}

export interface DeviceBase {
	deviceBaseId: number;
	deviceBaseName: string;
	deviceBaseProducer: string;
	deviceBaseYearOfRelease: number;
	noOfUnitsAvailable: number;
	deviceBaseImageUrl: string;
	deviceBaseUnits: Device[];
}

export default interface Device {
	deviceNumberOfControllers: number;
	deviceIsAvailable: boolean;
	deviceId: number;
	deviceBaseId: number;
	deviceName: string;
}

export interface Rental {
	rentalId: number;
	rentalDueDate: string;
	rentalReturnDate?: string;
	rentalClientId: number;
	rentalDeviceId: number;
	rentalDevice: Device;
}

export interface GameCopy {
	gameBase: {
		gameName: string;
		gameGenre: string;
	};
	gameDevice: {
		deviceBaseName: string;
		deviceBaseProducer: string;
		deviceBaseYearOfRelease: number;
	};
	gameId: number;
	gameCopyId: number;
	available: boolean;
}

export interface EntityException {
	message: string;
	details?: string;
	fieldName?: string;
}

export class APIResponse<T> {
	ok: boolean = false;
	status: number;
	body: T | EntityException | string;

	constructor(
		ok: boolean,
		status: number,
		body: T | EntityException | string
	) {
		this.ok = ok;
		this.status = status;
		this.body = body;
	}
}

export interface PageableResponse<T> {
	items: T[];
	totalPages: number;
}
