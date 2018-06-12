import '@main/styles/core.css';
import * as React from 'react';
import { Button, Message } from 'semantic-ui-react';

export interface Props {
    loadNextFactor: () => void
    display: boolean
}

const GenerateFactors: React.SFC<Props> = (props) => {
    return (
        <>
            {props.display &&
                <Message info>
                    <Message.Header>
                        Your challenge for today is to solve a multiplication equation.
                    </Message.Header>
                    <div className='margin-top-10'>
                        <Button color='blue' onClick={props.loadNextFactor}>Generate</Button>
                    </div>
                </Message>
            }
        </>
    )
}

export default GenerateFactors