import React, {Component} from 'react';
import {View, Text} from 'react-native';
import * as colors from '../styles/colors';
import {MediaQueryStyleSheet} from 'react-native-responsive';
import GraphView from '../native/GraphView';

import TcpSocket from 'react-native-tcp-socket';
import {NativeModules} from 'react-native';

export default class NetworkClient extends Component {
  static lock = 0;
  my_lock = 0;

  constructor(props) {
    super(props);

    this.state = {
      ping: 0,
      status: 'Init network',
      loop: 0,
    };

    this.graphRef = null;
  }

  render() {
    let my_text;
    let my_style;
    if (this.state.status !== 'ok') {
      my_text = this.state.status;
      my_style = styles.infoErrorText;
    } else if (this.state.ping > 0 && this.state.ping < 1000) {
      my_text = this.state.ping + ' ' + this.state.loop;
      my_style = styles.infoText;
    } else {
      my_text = this.state.ping + ' ' + this.state.loop;
      my_style = styles.infoErrorText;
    }
    return (
      <View style={styles.infoContainer}>
        <Text style={my_style}> {my_text} </Text>
      </View>
    );
  }

  componentDidMount() {
    this.client = null;
    this.loop = 0;
    this.startTime = 0;
    this.interval = setInterval(async () => {
      if (this.loop % 10 === 0) {
        console.log(
          'loop ' +
            this.loop +
            ' -- ' +
            this.state.status +
            ' -- ' +
            this.startTime,
        );
      }
      if (this.client === null) {
        if (this.loop % 10 === 0) {
          this.startTime = 0;
          this.connectToNetwork();
        }
      } else {
        if (this.loop % 10 === 0) {
          //send ping
          if (this.startTime === 0) {
            this.startTime = new Date();
            this.pingNb = this.state.loop;
            this.client.write('P' + this.pingNb + 'X');
          } else if (new Date() - this.startTime > 10000) {
            this.state.loop++;
            this.startTime = new Date();
            this.pingNb = this.state.loop;
            this.client.write('P' + this.pingNb + 'X');
          }
        }
      }
      if (this.loop > 1 && this.lock !== this.interval) {
        if (this.client) {
          this.client.destroy();
          this.client = null;
        }
        clearInterval(this.interval);
      }
      this.readData();
      this.loop = this.loop + 1;
    }, 100);
    this.lock = this.interval;
  }

  componentWillUnmount() {
    if (this.interval === this.lock) {
      this.lock = 0;
    }
  }

  async readData() {
    NativeModules.DataManager.readData(
      async (err) => {
        console.log(err);
      },
      async (msg) => {
        if (msg != null && msg.length > 0) {
          if (this.client !== null && this.state.status === 'ok') {
            console.log('send ' + msg.length);
            this.client.write(msg);
          } else {
            console.log(msg.length);
          }
        }
      },
    );
  }

  connectToNetwork() {
    console.log('Connect to network...');
    this.setState({status: 'connecting...'});
    this.client = TcpSocket.createConnection({
      port: 6543,
      host: '192.168.5.251',
      //tls: true,
      // tlsCheckValidity: false, // Disable validity checking
      // tlsCert: require('./selfmade.pem') // Self-signed certificate
    });
    if (this.client) {
      this.client.on('data', (data) => {
        console.log('Message received', data);
        //P
        if (data[0] === 80) {
          console.log('Message received ping');
          let i = 1;
          let nb = 0;
          //X
          while (i < data.length && data[i] !== 88) {
            nb = nb * 10 + data[i] - 48;
            i++;
          }
          console.log('Message received ping ', nb);
          if (nb === this.state.loop) {
            this.setState({
              status: 'ok',
              loop: this.state.loop + 1,
              ping: new Date() - this.startTime,
            });
            this.startTime = 0;
          } else {
            this.setState({
              status: 'ok',
              loop: this.state.loop + 1,
              ping: -1,
            });
            this.startTime = 0;
          }
        } else {
          this.setState({
            status: 'ok',
            loop: this.state.loop + 1,
            ping: -2,
          });
          this.startTime = 0;
        }
      });

      this.client.on('error', (error) => {
        this.setState({status: 'error'});
        console.log('Network error', error);
      });

      this.client.on('close', () => {
        this.setState({status: 'close'});
        console.log('Connection close');
        if (this.client) {
          this.client.destroy();
          this.client = null;
        }
      });
    } else {
      this.setState({status: 'error connection'});
    }
  }
}

const styles = MediaQueryStyleSheet.create(
  // Base styles
  {
    infoContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      paddingRight: 5,
    },

    infoStyle: {
      width: 45,
    },

    infoText: {
      textAlign: 'center',
      color: colors.white,
      fontFamily: 'Roboto-Medium',
      fontSize: 15,
    },

    infoErrorText: {
      textAlign: 'center',
      color: colors.red,
      fontFamily: 'Roboto-Medium',
      fontSize: 15,
    },
  },
);
