import { HTTPStatusCode } from '@main/http/utils';
import { Reducer } from 'redux';
import { ActionTypes, GameStat, StatisticAction } from '../types';

export interface LeaderboardRow {
    userId: number,
    totalScore: number
}

export interface StatisticState {
    isLoading: boolean
    serverError: string | null
    statistic: GameStat | null
}

const initialState: StatisticState = {
    isLoading: false,
    serverError: null,
    statistic: null
}

const statistic: Reducer<StatisticState, StatisticAction> =
    (state: StatisticState = initialState, action: StatisticAction) => {
        switch (action.type) {
            case ActionTypes.STATISTIC_REQUEST: {
                return { ...state, isLoading: true, serverError: null }
            }
            case ActionTypes.STATISTIC_SUCCESS: {
                return { ...state, isLoading: false, serverError: null, statistic: action.statistic }
            }
            case ActionTypes.STATISTIC_FAILURE: {
                if (action.reason.response.status === HTTPStatusCode.NOT_FOUND) {
                    return {
                        isLoading: false,
                        serverError: 'No statistics are available until you enter a correct answer',
                        statistic: null
                    }
                } else {
                    return {
                        isLoading: false,
                        serverError: action.reason.error.message,
                        statistic: null
                    }           
                }      
            }
            default: return state
        }
    }

export const statisticReducer: Reducer<StatisticState, StatisticAction> = statistic