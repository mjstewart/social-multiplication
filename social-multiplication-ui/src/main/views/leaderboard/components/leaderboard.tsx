import { RootState } from '@main/redux';
import * as React from 'react';
import { connect, Dispatch } from 'react-redux';
import { Button, Loader, Message, Table } from 'semantic-ui-react';
import { asyncLeaderboard } from '../actions';
import { LeaderboardState } from '../reducers/reducer';
import { LeaderboardAction, LeaderboardRequest } from '../types';

const Leaderboard: React.SFC<Props> = ({ leaderboardState, onRefresh }) => {
    return <div>
        <h1>Leaderboard</h1>
        {leaderboardState.isLoading && <Loader active inline />}

        {!leaderboardState.isLoading && leaderboardState.serverError &&
            <Message error>
                <Message.Header>Oops, there's been an issue contacting the server</Message.Header>
                <p>{leaderboardState.serverError}</p>
            </Message>
        }

        {!leaderboardState.isLoading && <Button onClick={onRefresh}>Refresh</Button>}

        {leaderboardState.leaderboard.length > 0 &&
            <Table>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell>User ID</Table.HeaderCell>
                        <Table.HeaderCell>Score</Table.HeaderCell>
                    </Table.Row>
                </Table.Header>

                <Table.Body>
                    {leaderboardState.leaderboard.map(it =>
                        <Table.Row key={it.userId}>
                            <Table.Cell>{it.userId}</Table.Cell>
                            <Table.Cell>{it.totalScore}</Table.Cell>
                        </Table.Row>
                    )}
                </Table.Body>
            </Table>
        }
    </div>
};


type Props = StateToProps & DispatchToProps

type StateToProps = {
    leaderboardState: LeaderboardState
}

const mapStateToProps = (state: RootState): StateToProps => ({
    leaderboardState: state.leaderboard
});

type DispatchToProps = {
    onRefresh: () => LeaderboardRequest
}

const mapDispatchToProps = (dispatch: Dispatch<LeaderboardAction>): DispatchToProps => ({
    onRefresh: () => dispatch(asyncLeaderboard.request())
});

export default connect(mapStateToProps, mapDispatchToProps)(Leaderboard);