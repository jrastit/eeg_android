// FeatureChart.js
// A chart to help people understand the features in the classifier

import React, { Component } from "react";
import { Image, Text, View,} from "react-native";
import * as colors from "../styles/colors";

export default class MiniChart extends Component {
  constructor(props) {
    super(props);
  }

  getElectrodeData(electrode, data) {
    const electrodeData = data.slice((electrode - 1) * 4, electrode * 4);
    return [{x: "δ", y: electrodeData[0], width: 8}, {x: "θ", y: electrodeData[1], width: 8}, {x: "α", y: electrodeData[2], width: 8}, {x: "β", y: electrodeData[3], width: 8}]
  }

  render() {
    return (
      <View
        style={{
          alignItems: "center",
          backgroundColor: colors.white,
        }}
      >
        <View
          style={{
            flexDirection: "row",
            alignItems: "flex-start",
            justifyContent: "space-between"
          }}
        >
          <Text
            style={{
              position: "absolute",
              left: -20,
              bottom: 75,
              fontWeight: "100",
              color: colors.black,
              fontFamily: "Roboto-Light",
              fontSize: 11,
              transform: [{ rotate: "270deg" }]
            }}
          >
            {" "}Feature Power
          </Text>
        </View>

        <Image
          source={require("../assets/electrodeheadlegend.png")}
          style={{ width: 75, height: 75, marginTop: -20, marginLeft: 55}}
          resizeMode="contain"
        />
      </View>
    );
  }
}
