import { all } from 'redux-saga/effects'

import { sagas as attemptHistorySagas } from '@views/attempt-history';
import { sagas as leaderboardSagas } from '@views/leaderboard';
import { sagas as multiplicationSagas } from '@views/multiplication';
import { sagas as statisticSagas } from '@views/statistics';

export function* rootSaga() {
    yield all([
        multiplicationSagas(),
        attemptHistorySagas(),
        leaderboardSagas(),
        statisticSagas()
    ]);
}
 