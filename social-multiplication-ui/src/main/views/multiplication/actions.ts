import {
    ActionTypes,
    AsyncAnswerAttempt,
    AsyncRandomFactors,
    Multiplication,
    MultiplicationAttempt
} from './types';

export const asyncRandomFactors: AsyncRandomFactors = ({
    request: () => ({ type: ActionTypes.RANDOM_FACTORS_REQUEST }),
    success: (multiplication: Multiplication) => ({
        type: ActionTypes.RANDOM_FACTORS_SUCCESS,
        factors: multiplication
    }),
    failure: (reason: string) => ({
        type: ActionTypes.RANDOM_FACTORS_FAILURE,
        reason
    })
});

export const asyncAnswerAttempts: AsyncAnswerAttempt = ({
    request: (attempt: MultiplicationAttempt) => ({
        type: ActionTypes.ATTEMPT_REQUEST,
        attempt,
    }),
    success: (result: MultiplicationAttempt) => ({
        type: ActionTypes.ATTEMPT_SUCCESS,
        result
    }),
    failure: (reason: string) => ({
        type: ActionTypes.ATTEMPT_FAILURE,
        reason
    })
});
