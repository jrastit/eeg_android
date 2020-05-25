// BCIHistoryChart.js
// A dynamic line chart showing the history of BCI outputs

import React, { Component } from "react";
import { View } from "react-native";
import * as colors from "../styles/colors";

export default class BCIHistoryChart extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <View
        style={{
          alignItems: "center",
          backgroundColor: colors.skyBlue,
          height: this.props.height,
          width: this.props.width
        }}
      >
      </View>
    );
  }
}
