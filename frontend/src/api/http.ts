export class ApiError extends Error {

  readonly status: number
  readonly details?: unknown

  constructor(
    message: string,
    status: number,
    details?: unknown
  ) {
    super(message)

    this.name = 'ApiError'
    this.status = status
    this.details = details
  }
}

export async function apiRequest<T>(
  url: string,
  options: RequestInit = {}
): Promise<T> {

  const response = await fetch(url, {
    ...options,

    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    }
  })

  if (!response.ok) {

    let details: unknown

    try {
      details = await response.json()
    } catch {
      details = undefined
    }

    throw new ApiError(
      `HTTP ${response.status}`,
      response.status,
      details
    )
  }

  if (response.status === 204) {
    return undefined as T
  }

  return await response.json() as T
}