import { HttpError } from "@main/http/utils";
import { Action } from "redux";

export interface GameStat {
    userId: number
    score: number
    newlyAwardedBadges: string[]
}

export enum ActionTypes {
    STATISTIC_REQUEST = '@@statistic/STATISTIC_REQUEST',
    STATISTIC_SUCCESS = '@@statistic/STATISTIC_SUCCESS',
    STATISTIC_FAILURE = '@@statistic/STATISTIC_FAILURE',
}

export type StatisticAction =
    StatisticRequest
    | StatisticSuccess
    | StatisticFailure

export interface StatisticRequest extends Action {
    type: ActionTypes.STATISTIC_REQUEST
    userId: number
}

export interface StatisticSuccess extends Action {
    type: ActionTypes.STATISTIC_SUCCESS
    statistic: GameStat
}

export interface StatisticFailure extends Action {
    type: ActionTypes.STATISTIC_FAILURE
    reason: HttpError
}

export interface AsyncStatistic {
    request: (userId: number) => StatisticRequest
    success: (statistic: GameStat) => StatisticSuccess
    failure: (reason: HttpError) => StatisticFailure
}