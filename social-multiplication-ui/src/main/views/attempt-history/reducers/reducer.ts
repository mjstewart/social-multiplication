import { MultiplicationAttempt } from '@main/views/multiplication/types';
import { Reducer } from 'redux';
import { ActionTypes, AttemptHistoryAction } from '../types';

export interface AttemptHistoryState {
    isLoading: boolean
    serverError: string | null
    history: MultiplicationAttempt[]
}

const attemptHistoryInitialState: AttemptHistoryState = {
    isLoading: false,
    serverError: null,
    history: [],
}

const attemptHistory: Reducer<AttemptHistoryState, AttemptHistoryAction> =
    (state: AttemptHistoryState = attemptHistoryInitialState, action: AttemptHistoryAction) => {
        switch (action.type) {
            case ActionTypes.HISTORY_REQUEST: {
                return { ...state, isLoading: true, serverError: null }
            }
            case ActionTypes.HISTORY_SUCCESS: {
                return { ...state, isLoading: false, serverError: null, history: action.history }
            }
            case ActionTypes.HISTORY_FAILURE: {
                return { ...state, isLoading: false, serverError: action.reason }
            }
            default: return state
        }
    }

export const attemptHistoryReducer: Reducer<AttemptHistoryState, AttemptHistoryAction> = attemptHistory