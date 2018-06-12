import { Reducer } from "redux";
import {
    ActionTypes,
    Multiplication,
    MultiplicationAction,
    MultiplicationAttempt
} from "../types";

export interface MultiplicationState {
    isLoading: boolean
    serverError: string | null
    currentFactor: Multiplication | null
    lastAttempt: MultiplicationAttempt | null
}

const initialState: MultiplicationState = {
    isLoading: false,
    serverError: null,
    currentFactor: null,
    lastAttempt: null,
}

const attempt: Reducer<MultiplicationState, MultiplicationAction> =
    (state: MultiplicationState = initialState, action: MultiplicationAction) => {
        switch (action.type) {
            case ActionTypes.RANDOM_FACTORS_REQUEST: {
                return { ...state, isLoading: true, currentFactor: null, serverError: null, lastAttempt: null }
            }
            case ActionTypes.RANDOM_FACTORS_SUCCESS: {
                return { ...state, isLoading: false, serverError: null, currentFactor: action.factors }
            }
            case ActionTypes.RANDOM_FACTORS_FAILURE: {
                return { ...state, isLoading: false, serverError: action.reason }
            }
            case ActionTypes.ATTEMPT_REQUEST: {
                return { ...state, isLoading: true, serverError: null }
            }
            case ActionTypes.ATTEMPT_SUCCESS: {
                return { ...state, isLoading: false, serverError: null, lastAttempt: action.result }
            }
            case ActionTypes.ATTEMPT_FAILURE: {
                return { ...state, isLoading: false, serverError: action.reason }
            }
            default: return state
        }
    }


// /**
//  * combineReducers will grab the multiplication key from the global app state and pass it into the reducer.
//  */
// export const multiplicationReducer: Reducer<MultiplicationState, T.MultiplicationAction> = combineReducers<MultiplicationState, T.MultiplicationAction>({
//     multiplication: factorQuizReducer
// });

/**
 * I am not slicing the 'multiplication state' up any more so it doesnt make sense to use combineReducers as otherwise I would be asking for a reducer for
 * each key in the multiplication state.
 * 
 * This shows an example of doing just that.
 * https://github.com/piotrwitek/react-redux-typescript-guide/blob/master/playground/src/features
 */
export const multiplicationReducer: Reducer<MultiplicationState, MultiplicationAction> = attempt;