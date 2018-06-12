import { MultiplicationAttempt } from "@main/views/multiplication/types";
import { Action } from "redux";

export enum ActionTypes {
    HISTORY_REQUEST = '@@attempts/HISTORY_REQUEST',
    HISTORY_SUCCESS = '@@attempts/HISTORY_SUCCESS',
    HISTORY_FAILURE = '@@attempts/HISTORY_FAILURE',
}

export type AttemptHistoryAction =
    AttemptHistoryRequest
    | AttemptHistorySuccess
    | AttemptHistoryFailure

// Retrieve attempt history

export interface AttemptHistoryRequest extends Action {
    type: ActionTypes.HISTORY_REQUEST
    userAlias: string
}

export interface AttemptHistorySuccess extends Action {
    type: ActionTypes.HISTORY_SUCCESS
    history: MultiplicationAttempt[]
}

export interface AttemptHistoryFailure extends Action {
    type: ActionTypes.HISTORY_FAILURE
    reason: string
}

export interface AsyncAttemptHistory {
    request: (userAlias: string) => AttemptHistoryRequest
    success: (history: MultiplicationAttempt[]) => AttemptHistorySuccess
    failure: (reason: string) => AttemptHistoryFailure
}