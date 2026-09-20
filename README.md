# Blowpipe Calculator

A simple RuneLite sidebar calculator for planning Toxic blowpipe supplies.

## Features

- **Darts → Scales:** enter how many darts you have and estimate how many Zulrah scales will be used while firing them.
- **Scales → Darts:** enter a scale amount and estimate how many darts are needed to use those scales.
- **Cape slot selector:** choose the ammo-saving effect you use.
- **Live calculation:** results update automatically as the amount or cape-slot selection changes.
- **Persistent selection:** your selected ammo-saving option is remembered by RuneLite.

## Cape slot options

- None — 0%
- Ava's attractor — 60%
- Ava's accumulator — 72%
- Ava's assembler — 80%
- Dizana's quiver (upgraded) — 80%

Dizana's quiver only provides the assembler's 80% ammunition-saving effect after the assembler effect has been applied to the quiver through Ava.

## Calculation

The Toxic blowpipe consumes, on average, **2 Zulrah scales per 3 attacks**. Ammo-saving effects reduce the number of darts consumed, but they do not reduce the blowpipe's scale consumption per attack.

Because ammunition recovery and scale consumption are probabilistic, results are **estimates based on average rates**, not guaranteed exact consumption.

## Support development

Blowpipe Calculator is free and all features are available to everyone. If the plugin has helped you and you would like to support continued development, you can [buy me a coffee](https://buymeacoffee.com/ebpicgamer). Contributions are entirely optional and do not unlock additional features.

## Development

Run the tests:

```text
./gradlew test
```

Launch the RuneLite test client:

```text
./gradlew run
```

This plugin has no third-party dependencies or network access.
