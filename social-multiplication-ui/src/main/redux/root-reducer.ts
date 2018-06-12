import { RootAction } from "@main/redux/root-action";
import { AttemptHistoryState } from "@main/views/attempt-history/reducers/reducer";
import { LeaderboardState } from "@main/views/leaderboard/reducers/reducer";
import { reducer as attemptReducer } from "@views/attempt-history";
import { reducer as leaderboardReducer } from "@views/leaderboard";
import { reducer as multiplicationReducer } from "@views/multiplication";
import { MultiplicationState } from "@views/multiplication/reducers/reducer";
import { reducer as statisticReducer } from "@views/statistics";
import { StatisticState } from "@views/statistics/reducers/reducer";
import { combineReducers, Reducer } from "redux";

export interface RootState {
    multiplication: MultiplicationState
    attemptHistory: AttemptHistoryState
    leaderboard: LeaderboardState
    statistic: StatisticState
}

/**
 * Accepts global app state and feeds through all reducers to create new app state.
 */
export const rootReducer: Reducer<RootState, RootAction> = combineReducers({
    multiplication: multiplicationReducer,
    attemptHistory: attemptReducer,
    leaderboard: leaderboardReducer,
    statistic: statisticReducer
});
