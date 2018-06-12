export enum HTTPStatusCode {
    OK = 200,
    CREATED = 201,
    BAD_REQUEST = 400,
    UNAUTHORIZED = 401,
    NOT_FOUND = 404,
    UNKNOWN_ERROR = 9999,
}

export interface HttpError {
    error: Error,
    response: Response
}

const httpError = (error: Error, response: Response) => ({
    error,
    response
})

export const headers = new Headers({
    'Content-Type': 'application/json',
    Accept: 'application/json',
});

function checkHttpError(response: Response): Promise<Response> {
    if (response.ok) {
        return Promise.resolve(response);
    }
    switch (response.status) {
        case HTTPStatusCode.NOT_FOUND: {
            return Promise.reject(httpError(new Error('Url not found'), response));
        }
        case HTTPStatusCode.BAD_REQUEST: {
            return Promise.reject(httpError(new Error('Bad request'), response));
        }
        case HTTPStatusCode.UNAUTHORIZED: {
            return Promise.reject(httpError(new Error('Unauthorized'), response));
        }
        default: return Promise.reject(httpError(new Error('Unknown error'), response));
    }
}

function isResponseJson(response: Response): Promise<Response> {
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
        return Promise.resolve(response);
    }
    return Promise.reject(httpError(new Error('api response is not json'), response));
}

function toJson(response: Response): Promise<any> {
    return response.json();
}

export const validateResponseToJson = (response: Response): Promise<any> => {
    return checkHttpError(response)
        .then(isResponseJson)
        .then(toJson);
};

export async function get(url: string): Promise<any> {
    try {
        const response = await fetch(url, {
            headers,
            method: 'GET'
        });
        return await validateResponseToJson(response);
    } catch (e) {
        return Promise.reject(e);
    }
}

export async function post(url: string, payload: any): Promise<any> {
    try {
        const response = await fetch(url, {
            headers,
            method: 'POST',
            body: JSON.stringify(payload)
        });
        return await validateResponseToJson(response);
    } catch (e) {
        return Promise.reject(e);
    }
}