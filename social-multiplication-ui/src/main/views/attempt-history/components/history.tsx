import { RootState } from "@main/redux";
import '@main/styles/core.css';
import * as React from 'react';
import { connect } from "react-redux";
import { Header, Icon, Loader, Message, Table } from 'semantic-ui-react';
import { AttemptHistoryState } from '../reducers/reducer';

const AttemptHistory: React.SFC<Props> = ({ historyState: { history, isLoading, serverError } }) => {
    return (
        <>
            {isLoading && <Loader active inline>Loading the latest history...</Loader>}

            {serverError &&
                <Message error>
                    <Message.Header>Oops, there's been an issue contacting the server</Message.Header>
                    <p>{serverError}</p>
                </Message>
            }

            {history.length > 0 &&
                <>
                    <Header as='h1' icon='history' content={`Your latest attempts (${history.length})`} />
                    <Table>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell>Id</Table.HeaderCell>
                                <Table.HeaderCell>Alias</Table.HeaderCell>
                                <Table.HeaderCell>Multiplication</Table.HeaderCell>
                                <Table.HeaderCell>You entered</Table.HeaderCell>
                                <Table.HeaderCell>Correct?</Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>

                        <Table.Body>
                            {history.map(it =>
                                <Table.Row key={it.id!!}>
                                    <Table.Cell>{it.id}</Table.Cell>
                                    <Table.Cell>{it.user.alias}</Table.Cell>
                                    <Table.Cell>{`${it.multiplication.factorA} * ${it.multiplication.factorB}`}</Table.Cell>
                                    <Table.Cell>{it.resultAttempt}</Table.Cell>
                                    <Table.Cell>{it.correct === true ?
                                        <Icon name='checkmark' color='green' /> :
                                        <Icon name='remove' color='red' />
                                    }
                                    </Table.Cell>
                                </Table.Row>
                            )}
                        </Table.Body>
                    </Table>
                </>
            }
        </>
    )
}

type Props = StateToProps

type StateToProps = {
    historyState: AttemptHistoryState
}

const mapStateToProps = (state: RootState): StateToProps => ({
    historyState: state.attemptHistory
});

export default connect(mapStateToProps)(AttemptHistory);
