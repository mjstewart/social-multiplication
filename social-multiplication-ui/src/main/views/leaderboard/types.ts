import { LeaderboardRow } from "@main/views/leaderboard/reducers/reducer";
import { Action } from "redux";

export enum ActionTypes {
    LEADERBOARD_REQUEST = '@@leaderboard/LEADERBOARD_REQUEST',
    LEADERBOARD_SUCCESS = '@@leaderboard/LEADERBOARD_SUCCESS',
    LEADERBOARD_FAILURE = '@@leaderboard/LEADERBOARD_FAILURE',
}

export type LeaderboardAction =
    LeaderboardRequest
    | LeaderboardSuccess
    | LeaderboardFailure

export interface LeaderboardRequest extends Action {
    type: ActionTypes.LEADERBOARD_REQUEST
}

export interface LeaderboardSuccess extends Action {
    type: ActionTypes.LEADERBOARD_SUCCESS
    leaderboard: LeaderboardRow[]
}

export interface LeaderboardFailure extends Action {
    type: ActionTypes.LEADERBOARD_FAILURE
    reason: string
}

export interface AsyncLeaderboard {
    request: () => LeaderboardRequest
    success: (leaderboard: LeaderboardRow[]) => LeaderboardSuccess
    failure: (reason: string) => LeaderboardFailure
}