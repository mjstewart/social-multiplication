import { AttemptHistoryAction } from "@views/attempt-history";
import { LeaderboardAction } from "@views/leaderboard";
import { MultiplicationAction } from "@views/multiplication";
import { StatisticAction } from "@views/statistics";

export type RootAction =
    | MultiplicationAction
    | AttemptHistoryAction
    | LeaderboardAction
    | StatisticAction