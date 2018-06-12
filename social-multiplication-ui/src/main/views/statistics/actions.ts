import { HttpError } from '@main/http/utils';
import { ActionTypes, AsyncStatistic, GameStat } from './types';

export const asyncStatistic: AsyncStatistic = ({
    request: (userId: number) => ({
        type: ActionTypes.STATISTIC_REQUEST,
        userId
    }),
    success: (statistic: GameStat) => ({
        type: ActionTypes.STATISTIC_SUCCESS,
        statistic
    }),
    failure: (reason: HttpError) => ({
        type: ActionTypes.STATISTIC_FAILURE,
        reason
    })
});

 