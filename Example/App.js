import React from 'react';
import { FlatList, Image, StyleSheet, View } from 'react-native';
import Pinchable from 'react-native-pinchable';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5FCFF',
    padding: 5,
  },
  pinchable: {
    flex: 1,
    margin: 5,
  },
  image: {
    aspectRatio: 1,
    width: null,
    height: null,
  },
});

const DanTheMan = () => (
  <Pinchable style={styles.pinchable}>
    <Image source={require('./dannyboi.jpg')} style={styles.image} />
  </Pinchable>
)

export default () => (
  <FlatList
    data={new Array(20).fill(null).map((_,i) => i.toString())}
    numColumns={3}
    keyExtractor={item => item}
    renderItem={DanTheMan}
    style={styles.container}
  />
);
