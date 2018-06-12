import { rootReducer, rootSaga, RootState } from "@main/redux";
import { applyMiddleware, createStore } from "redux";
import { createLogger } from 'redux-logger';
import createSagaMiddleware from 'redux-saga';

const sagaMiddleware = createSagaMiddleware();

const configureStore = (initialState?: RootState) => {
    const middleware = applyMiddleware(sagaMiddleware, createLogger());

    if (initialState) {
        return createStore(rootReducer, initialState, middleware);
    }
    return createStore(rootReducer, middleware);
}

const store = configureStore();

sagaMiddleware.run(rootSaga);

export default store;