import { MultiplicationAttempt } from "@views/multiplication/types";
import { ActionTypes, AsyncAttemptHistory } from './types';

export const asyncHistory: AsyncAttemptHistory = ({
    request: (userAlias: string) => ({
        type: ActionTypes.HISTORY_REQUEST,
        userAlias,
    }),
    success: (history: MultiplicationAttempt[]) => ({
        type: ActionTypes.HISTORY_SUCCESS,
        history
    }),
    failure: (reason: string) => ({
        type: ActionTypes.HISTORY_FAILURE,
        reason
    })
});