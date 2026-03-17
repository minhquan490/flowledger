export interface Patient {
    id: string;
    name: string;
    identifier?: string;
    status?: string;
    gender?: string;
    birthDate?: string;
}

export interface Contact {
    id: string;
    name: string;
    relationship?: string;
    phone?: string;
    email?: string;
}

export interface Address {
    id: string;
    line1: string;
    line2?: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
    use?: "home" | "work" | "temp" | "old" | "billing";
}
