import { Reducer } from 'redux';
import { ActionTypes, LeaderboardAction } from '../types';

export interface LeaderboardRow {
    userId: number,
    totalScore: number
}

export interface LeaderboardState {
    isLoading: boolean
    serverError: string | null
    leaderboard: LeaderboardRow[]
}

const initialState: LeaderboardState = {
    isLoading: false,
    serverError: null,
    leaderboard: []
}

const leaderboard: Reducer<LeaderboardState, LeaderboardAction> =
    (state: LeaderboardState = initialState, action: LeaderboardAction) => {
        switch (action.type) {
            case ActionTypes.LEADERBOARD_REQUEST: {
                return { ...state, isLoading: true, serverError: null }
            }
            case ActionTypes.LEADERBOARD_SUCCESS: {
                return { ...state, isLoading: false, serverError: null, leaderboard: action.leaderboard }
            }
            case ActionTypes.LEADERBOARD_FAILURE: {
                return { ...state, isLoading: false, serverError: action.reason }
            }
            default: return state
        }
    }

export const leaderboardReducer: Reducer<LeaderboardState, LeaderboardAction> = leaderboard