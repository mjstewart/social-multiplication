import { RootState } from '@main/redux';
import { asyncStatistic } from '@views/statistics/actions';
import { StatisticAction, StatisticRequest } from '@views/statistics/types';
import * as React from 'react';
import { connect } from 'react-redux';
import { Dispatch } from 'redux';
import { Button, Loader, Message, Table } from 'semantic-ui-react';
import { StatisticState } from '../reducers/reducer';

const Statistic: React.SFC<Props> = (props) => {
    const { statisticsState: { isLoading, serverError, statistic } } = props;
    const { getStatistic, currentUserId } = props;

    return <div>

        <h1>Your statistics</h1>
        {statistic === null && serverError === null && <p>No statistics yet</p>}


        {!isLoading && currentUserId !== null &&
            <Button onClick={getStatistic(currentUserId)}>Refresh</Button>
        }

        {isLoading && <Loader active inline />}

        {!isLoading && serverError &&
            <Message error>
                <p>{serverError}</p>
            </Message>
        }

        {statistic !== null && serverError === null &&
            <Table>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell>User ID</Table.HeaderCell>
                        <Table.HeaderCell>Score</Table.HeaderCell>
                        <Table.HeaderCell>Badges</Table.HeaderCell>
                    </Table.Row>
                </Table.Header>

                <Table.Body>
                    <Table.Row key={statistic.userId}>
                        <Table.Cell>{statistic.userId}</Table.Cell>
                        <Table.Cell>{statistic.score}</Table.Cell>
                        <Table.Cell>{statistic.newlyAwardedBadges.join(', ')}</Table.Cell>
                    </Table.Row>
                </Table.Body>
            </Table>
        }
    </div>
};

type Props = StateToProps & DispatchToProps

type StateToProps = {
    statisticsState: StatisticState
    currentUserId: number | null
}

type DispatchToProps = {
    getStatistic: (userId: number) => () => StatisticRequest
}

const mapStateToProps = (state: RootState): StateToProps => ({
    statisticsState: state.statistic,
    currentUserId: state.multiplication.lastAttempt === null ? null : state.multiplication.lastAttempt.user.id
});

const mapDispatchToProps = (dispatch: Dispatch<StatisticAction>): DispatchToProps => ({
    getStatistic: (userId: number) => () => dispatch(asyncStatistic.request(userId)),
});

export default connect(mapStateToProps, mapDispatchToProps)(Statistic);