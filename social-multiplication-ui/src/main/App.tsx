import HistoryAttempts from '@views/attempt-history/components/history';
import Leaderboard from '@views/leaderboard/components/leaderboard';
import Multiplication from '@views/multiplication/containers/multiplication';
import Statistic from '@views/statistics/components/statistic';
import * as React from 'react';
import { Container, Grid, Header, Menu } from 'semantic-ui-react';

// TODO: use selectors

const App: React.SFC = () => (
  <Container>
    <Menu pointing secondary>
      <Menu.Item name='home' />
      <Menu.Menu position='right'>
        <Menu.Item name='logout' />
      </Menu.Menu>
    </Menu>

    <Header as='h1' dividing>
      Welcome to Social Multiplication
    </Header>

    <Grid columns={2}>
      <Grid.Row>
        <Grid.Column>
          <Multiplication />
        </Grid.Column>
        <Grid.Column>
          <Leaderboard />
        </Grid.Column>
      </Grid.Row>
      <Grid.Row>
        <Grid.Column>
          <Statistic />
        </Grid.Column>
        <Grid.Column>
          <HistoryAttempts />
        </Grid.Column>
      </Grid.Row>
    </Grid>

  </Container>
)

export default App;
