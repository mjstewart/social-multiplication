import { API_URL } from '@main/config';
import * as http from '@main/http/utils';
import { asyncLeaderboard } from '@views/leaderboard/actions';
import { LeaderboardRow } from '@views/leaderboard/reducers/reducer';
import { ActionTypes } from '@views/leaderboard/types';
import { all, call, put, takeLatest } from 'redux-saga/effects';

export function* helloSaga() {
    console.log("hello leaderboard saga!");
    yield 5;
}

export function* leaderboardRequest() {
    try {
        console.log('leaderboardRequest');
        const response = yield call(http.get, `${API_URL}/leaders`);
        const leaderboard = (response as LeaderboardRow[])
        console.log('leaderboard response: ', leaderboard);
        yield put(asyncLeaderboard.success(leaderboard));
    } catch (e) {
        const httpError = e as http.HttpError
        console.log('leaderboardRequest error: ', httpError);
        yield put(asyncLeaderboard.failure(httpError.error.message));
    }
}

function* onLeaderboardRequest() {
    yield takeLatest(ActionTypes.LEADERBOARD_REQUEST, leaderboardRequest);
}

export default function* rootSaga() {
    yield all([
        helloSaga(),
        onLeaderboardRequest(),
    ]);
}