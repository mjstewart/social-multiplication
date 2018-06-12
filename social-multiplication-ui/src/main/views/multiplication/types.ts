import { Action } from "redux";

export interface Multiplication {
    factorA: number
    factorB: number
}
 
export interface MultiplicationAttempt {
    id: number | null
    user: {
        id: number | null
        alias: string
    },
    resultAttempt: number
    multiplication: Multiplication
    correct: boolean
}

export type MultiplicationAction =
    RandomFactorActionRequest
    | RandomFactorActionSuccess
    | RandomFactorActionFailure
    | AttemptRequest
    | AttemptSuccess
    | AttemptFailure

export enum ActionTypes {
    RANDOM_FACTORS_REQUEST = '@@multiplication/RANDOM_FACTORS_REQUEST',
    RANDOM_FACTORS_SUCCESS = '@@multiplication/RANDOM_FACTORS_SUCCESS',
    RANDOM_FACTORS_FAILURE = '@@multiplication/RANDOM_FACTORS_FAILURE',

    ATTEMPT_REQUEST = '@@multiplication/ATTEMPT_REQUEST',
    ATTEMPT_SUCCESS = '@@multiplication/ATTEMPT_SUCCESS',
    ATTEMPT_FAILURE = '@@multiplication/ATTEMPT_FAILURE',
}

// get the random factors to multiply.

export interface RandomFactorActionRequest extends Action {
    type: ActionTypes.RANDOM_FACTORS_REQUEST
}

export interface RandomFactorActionSuccess extends Action {
    type: ActionTypes.RANDOM_FACTORS_SUCCESS
    factors: Multiplication
}

export interface RandomFactorActionFailure extends Action {
    type: ActionTypes.RANDOM_FACTORS_FAILURE
    reason: string
}

export interface AsyncRandomFactors {
    request: () => RandomFactorActionRequest
    success: (multiplication: Multiplication) => RandomFactorActionSuccess
    failure: (reason: string) => RandomFactorActionFailure
}


// Attempt to answer multiplication

export interface AttemptRequest extends Action {
    type: ActionTypes.ATTEMPT_REQUEST
    attempt: MultiplicationAttempt
}

export interface AttemptSuccess extends Action {
    type: ActionTypes.ATTEMPT_SUCCESS
    result: MultiplicationAttempt
}

export interface AttemptFailure extends Action {
    type: ActionTypes.ATTEMPT_FAILURE
    reason: string
}

export interface AsyncAnswerAttempt {
    request: (attempt: MultiplicationAttempt) => AttemptRequest
    success: (result: MultiplicationAttempt) => AttemptSuccess
    failure: (reason: string) => AttemptFailure
}



 