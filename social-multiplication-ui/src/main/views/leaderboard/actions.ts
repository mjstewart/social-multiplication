import { LeaderboardRow } from '@main/views/leaderboard/reducers/reducer';
import { ActionTypes, AsyncLeaderboard } from './types';

export const asyncLeaderboard: AsyncLeaderboard = ({
    request: () => ({
        type: ActionTypes.LEADERBOARD_REQUEST,
    }),
    success: (leaderboard: LeaderboardRow[]) => ({
        type: ActionTypes.LEADERBOARD_SUCCESS,
        leaderboard
    }),
    failure: (reason: string) => ({
        type: ActionTypes.LEADERBOARD_FAILURE,
        reason
    })
});