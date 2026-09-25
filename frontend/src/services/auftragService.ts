import { apiRequest } from '../api/http'

import type {
  Auftrag,
  AuftragRequest
} from '../types/auftrag'

const BASE_URL = '/api/auftraege'

export function ladeAuftraege():
  Promise<Auftrag[]> {

  return apiRequest<Auftrag[]>(
    BASE_URL
  )
}

export function legeAuftragAn(
  request: AuftragRequest
): Promise<Auftrag> {

  return apiRequest<Auftrag>(
    BASE_URL,
    {
      method: 'POST',
      body: JSON.stringify(request)
    }
  )
}

export function gebeAuftragFrei(
  auftragsNummer: string
): Promise<Auftrag> {

  return apiRequest<Auftrag>(
    `${BASE_URL}/${encodeURIComponent(auftragsNummer)}/freigeben`,
    {
      method: 'POST'
    }
  )
}