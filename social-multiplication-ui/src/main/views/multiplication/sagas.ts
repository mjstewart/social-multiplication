import { API_URL } from '@main/config';
import * as http from '@main/http/utils';
import { asyncHistory } from '@views/attempt-history/actions';
import { asyncAnswerAttempts, asyncRandomFactors } from '@views/multiplication/actions';
import { ActionTypes, AttemptRequest, MultiplicationAttempt } from '@views/multiplication/types';
import { asyncStatistic } from '@views/statistics/actions';
import { all, call, put, takeLatest } from 'redux-saga/effects';

export function* helloSaga() {
    console.log("hello multiplication saga!");
    yield 5;
}

function* randomFactorsRequest() {
    try {
        const multiplication = yield call(http.get, `${API_URL}/multiplications/random`);
        console.log("randomFactorsRequest response: ", multiplication);
        yield put(asyncRandomFactors.success(multiplication));
    } catch (e) {
        const httpError = e as http.HttpError
        yield put(asyncRandomFactors.failure(httpError.error.message));
    }
}

function* attemptRequest(request: AttemptRequest) {
    try {
        const response = yield call(http.post, `${API_URL}/results`, request.attempt);
        
        // The server will have populated any id's from persisting
        const attempt = (response as MultiplicationAttempt);

        yield put(asyncAnswerAttempts.success(attempt));
        yield put(asyncHistory.request(attempt.user.alias));
        yield put(asyncStatistic.request(attempt.user.id!!));
    } catch (e) {
        const httpError = e as http.HttpError
        yield put(asyncAnswerAttempts.failure(httpError.error.message));
    }
}

function* onRandomFactorsRequest() {
    yield takeLatest(ActionTypes.RANDOM_FACTORS_REQUEST, randomFactorsRequest)
}

function* onAttemptRequest() {
    yield takeLatest(ActionTypes.ATTEMPT_REQUEST, attemptRequest)
}

export default function* rootSaga() {
    yield all([
        helloSaga(),
        onRandomFactorsRequest(),
        onAttemptRequest(),
    ]);
}