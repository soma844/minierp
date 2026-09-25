import { apiRequest } from '../api/http'

import type {
  Konfiguration,
  KonfigurationRequest
} from '../types/konfiguration'

const BASE_URL = '/api/konfigurationen'

export function ladeKonfigurationen():
  Promise<Konfiguration[]> {

  return apiRequest<Konfiguration[]>(
    BASE_URL
  )
}

export function legeKonfigurationAn(
  request: KonfigurationRequest
): Promise<Konfiguration> {

  return apiRequest<Konfiguration>(
    BASE_URL,
    {
      method: 'POST',
      body: JSON.stringify(request)
    }
  )
}

export function pruefeKonfiguration(
  nummer: string
): Promise<unknown[]> {

  return apiRequest<unknown[]>(
    `${BASE_URL}/${encodeURIComponent(nummer)}/pruefen`,
    {
      method: 'POST'
    }
  )
}

export function gebeKonfigurationFrei(
  nummer: string
): Promise<Konfiguration> {

  return apiRequest<Konfiguration>(
    `${BASE_URL}/${encodeURIComponent(nummer)}/freigeben`,
    {
      method: 'POST'
    }
  )
}

export function loescheKonfiguration(
  nummer: string
): Promise<void> {

  return apiRequest<void>(
    `${BASE_URL}/${encodeURIComponent(nummer)}`,
    {
      method: 'DELETE'
    }
  )
}