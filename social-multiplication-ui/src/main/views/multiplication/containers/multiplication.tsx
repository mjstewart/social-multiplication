import { RootState } from '@main/redux';
import { asyncAnswerAttempts, asyncRandomFactors } from '@views/multiplication/actions';
import { MultiplicationState } from '@views/multiplication/reducers/reducer';
import {
    AttemptRequest,
    MultiplicationAction,
    MultiplicationAttempt,
    RandomFactorActionRequest
} from '@views/multiplication/types';
import * as React from 'react';
import { connect, Dispatch } from 'react-redux';
import { Button, Form, Label, Loader, Message, Statistic } from 'semantic-ui-react';
import GenerateFactors from '../components/generate-factor';

type Field = 'name' | 'answer'

interface State {
    name: string
    answer: number
    validationErrors: Map<Field, string>
    globalErrors: string[]
}

class MultiplicationAnswerForm extends React.Component<Props, State> {

    public state: State = {
        answer: 0,
        name: '',
        validationErrors: new Map<Field, string>(),
        globalErrors: []
    }

    public render() {
        const { multiplicationState: { isLoading, currentFactor } } = this.props;

        return (
            <>
                {isLoading && <Loader active inline />}

                <GenerateFactors display={!isLoading} loadNextFactor={this.loadNextFactor} />

                {currentFactor &&
                    <div className='margin-top-10'>
                        <Message attached info>
                            <Message.Header>
                                What is the answer of multiplying these 2 numbers?
                            </Message.Header>
                            <Statistic>
                                <Statistic.Value>
                                    {`${currentFactor.factorA} * ${currentFactor.factorB}`}
                                </Statistic.Value>
                            </Statistic>
                        </Message>
                        <Form className='attached fluid segment'>
                            <Form.Field>
                                <input placeholder='Name' type='text' value={this.state.name} onChange={this.handleNameChange} />
                                {this.state.validationErrors.get('name') &&
                                    <Label basic color='red' pointing>{this.state.validationErrors.get('name')}</Label>
                                }
                            </Form.Field>

                            <Form.Field>
                                <input placeholder='Answer' type='number' min="1" value={this.state.answer} onChange={this.handleAnswerChange} />
                                {this.state.validationErrors.get('answer') &&
                                    <Label basic color='red' pointing>{this.state.validationErrors.get('answer')}</Label>
                                }
                            </Form.Field>

                            <Button color='blue' onClick={this.onSubmit} disabled={this.isCorrectAnswer(this.props)}>Submit</Button>
                        </Form>
                    </div>
                }

                {this.getFooter(this.state, this.props)}
            </>
        )
    }

    private isCorrectAnswer = (props: Props): boolean => {
        const { lastAttempt } = props.multiplicationState;
        return lastAttempt !== null && lastAttempt.correct;
    }

    private getFooter = (state: State, props: Props) => {
        const { serverError, lastAttempt, isLoading } = props.multiplicationState;

        if (isLoading === true) {
            return null;
        }

        if (serverError) {
            return (
                <Message error>
                    <Message.Header>Oops, there's been an issue contacting the server</Message.Header>
                    <p>{serverError}</p>
                </Message>
            )
        }

        if (state.globalErrors.length > 0) {
            return (
                <Message error
                    header='There was some errors with your submission'
                    list={state.globalErrors}
                />
            )
        }

        if (lastAttempt !== null) {

            if (lastAttempt.correct === true) {
                return (
                    <Message positive>
                        Congrats!, your answer is correct
                    </Message>
                )
            }
            return (
                <Message negative>
                    Sorry, your answer is incorrect. Try again
                </Message>
            )
        }

        return null;
    }

    private handleNameChange = (event: any) => {
        this.setState({ name: event.target.value })
    }

    private handleAnswerChange = (event: any) => {
        this.setState({ answer: event.target.value })
    }

    private loadNextFactor = () => {
        this.setState({
            name: '',
            answer: 0,
            validationErrors: new Map<Field, string>(),
            globalErrors: []
        });
        this.props.getRandomFactors();
    }

    private onSubmit = (event: any) => {
        event.preventDefault();

        let newValidationErrors = new Map<Field, string>();

        if (this.state.name.trim() === '') {
            newValidationErrors = newValidationErrors.set('name', 'required');
        }

        const isValidNumber = /[1-9][0-9]?/
        if (!isValidNumber.test(this.state.answer.toString())) {
            newValidationErrors = newValidationErrors.set('answer', 'Not a valid number');
        }

        if (newValidationErrors.size > 0) {
            this.setState({
                validationErrors: newValidationErrors
            });
            return;
        }

        const { currentFactor } = this.props.multiplicationState;
        if (currentFactor === null) {
            this.setState({
                globalErrors: [...this.state.globalErrors, 'Missing multiplication to solve']
            });
            return;
        }

        this.setState({
            validationErrors: new Map<Field, string>(),
            globalErrors: []
        });

        this.props.attemptAnswer({
            id: null,
            user: {
                id: null,
                alias: this.state.name.trim()
            },
            resultAttempt: this.state.answer,
            multiplication: currentFactor,
            correct: false
        });
    }
}

type Props = StateToProps & DispatchToProps

type StateToProps = {
    multiplicationState: MultiplicationState
}

const mapStateToProps = (state: RootState): StateToProps => ({
    multiplicationState: state.multiplication
});

type DispatchToProps = {
    getRandomFactors: () => RandomFactorActionRequest
    attemptAnswer: (attempt: MultiplicationAttempt) => AttemptRequest
}

const mapDispatchToProps = (dispatch: Dispatch<MultiplicationAction>): DispatchToProps => ({
    getRandomFactors: () => dispatch(asyncRandomFactors.request()),
    attemptAnswer: (attempt: MultiplicationAttempt) => dispatch(asyncAnswerAttempts.request(attempt))
});

export default connect(mapStateToProps, mapDispatchToProps)(MultiplicationAnswerForm);
