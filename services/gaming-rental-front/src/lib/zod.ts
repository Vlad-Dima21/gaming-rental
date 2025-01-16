import { z } from 'zod'

export const clientValidation = z.object({
    clientName: z.string().min(3, 'Client name should be 3-20 characters long').max(20, 'Client name should be 3-20 characters long'),
    clientEmail: z.string().email(),
    clientPhone: z.string().regex(/^(07[0-8][0-9]|02[0-9]{2}|03[0-9]{2})(\s|\.|-)?([0-9]{3}(\s|\.|-|)){2}$/),
})