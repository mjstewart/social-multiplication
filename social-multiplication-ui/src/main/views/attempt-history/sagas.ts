import { API_URL } from '@main/config';
import * as http from '@main/http/utils';
import { asyncHistory } from '@views/attempt-history/actions';
import * as Types from '@views/attempt-history/types';
import { MultiplicationAttempt } from '@views/multiplication/types';
import { all, call, put, takeLatest } from 'redux-saga/effects';

export function* helloSaga() {
    console.log("hello attempt-history saga!");
    yield 5;
}

export function* historyRequest(request: Types.AttemptHistoryRequest) {
    try {
        const response = yield call(http.get, `${API_URL}/results?alias=${request.userAlias}`);
        const history = (response as MultiplicationAttempt[])
        yield put(asyncHistory.success(history));
    } catch (e) {
        const httpError = e as http.HttpError
        yield put(asyncHistory.failure(httpError.error.message));
    }
}

function* onAttemptRequest() {
    yield takeLatest(Types.ActionTypes.HISTORY_REQUEST, historyRequest);
}

export default function* rootSaga() {
    yield all([
        helloSaga(),
        onAttemptRequest(),
    ]);
}