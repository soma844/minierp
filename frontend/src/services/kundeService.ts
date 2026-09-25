import { apiRequest } from '../api/http'
import type {
  Kunde,
  KundeRequest
} from '../types/kunde'

const BASE_URL = '/api/kunden'

export function ladeKunden(): Promise<Kunde[]> {

  return apiRequest<Kunde[]>(
    BASE_URL
  )
}

export function ladeKunde(
  kundenNummer: string
): Promise<Kunde> {

  return apiRequest<Kunde>(
    `${BASE_URL}/${encodeURIComponent(kundenNummer)}`
  )
}

export function legeKundeAn(
  request: KundeRequest
): Promise<Kunde> {

  return apiRequest<Kunde>(
    BASE_URL,
    {
      method: 'POST',
      body: JSON.stringify(request)
    }
  )
}

export function aktualisiereKunde(
  kundenNummer: string,
  request: KundeRequest
): Promise<Kunde> {

  return apiRequest<Kunde>(
    `${BASE_URL}/${encodeURIComponent(kundenNummer)}`,
    {
      method: 'PUT',
      body: JSON.stringify(request)
    }
  )
}

export function loescheKunde(
  kundenNummer: string
): Promise<void> {

  return apiRequest<void>(
    `${BASE_URL}/${encodeURIComponent(kundenNummer)}`,
    {
      method: 'DELETE'
    }
  )
}