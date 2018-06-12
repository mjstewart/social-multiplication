import { API_URL } from '@main/config';
import * as http from '@main/http/utils';
import { asyncStatistic } from '@views/statistics/actions';
import { ActionTypes, GameStat, StatisticRequest } from '@views/statistics/types';
import { all, call, put, takeLatest } from 'redux-saga/effects';

export function* helloSaga() {
    console.log("hello statistic saga!");
    yield 5;
}

export function* statisticRequest(request: StatisticRequest) {
    try {
        const response = yield call(http.get, `${API_URL}/stats?userId=${request.userId}`);
        const stat = (response as GameStat)
        console.log("stat request response: ", response);
        yield put(asyncStatistic.success(stat));
    } catch (e) {
        const httpError = e as http.HttpError
        yield put(asyncStatistic.failure(httpError));
    }
}

function* onStatisticRequest() {
    yield takeLatest(ActionTypes.STATISTIC_REQUEST, statisticRequest);
}

export default function* rootSaga() {
    yield all([
        helloSaga(),
        onStatisticRequest(),
    ]);
}