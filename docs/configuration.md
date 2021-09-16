---
hide:
  - navigation
---
# Configuration

## Defaults

Below you will find the default configuration file `v3` for VestriaDice version `2.2.x`.

``` yaml
--8<-- "src/main/resources/config.yml"
```

## Applying Changes

After you edited the configuration to your liking you may load those changes by executing `/vdice reload` either inside the console or from in-game.

## Custom Translations

As the first comment inside the default configuration tells you, you have the possibility to customize nearly any text displayed inside the plugin. For that you create a new `.properties`-file inside the plugins `lang`-folder. You can either create a translation by creating a `lang_<language>_<country>.properties`-file (e.g. `lang_fr_FR.properties`) translating all strings inside the default `lang.properties`-file, or you can customize an already existing language by creating a `lang_<existing-language>_<existing-country>_custom.properties`-file (e.g. `lang_de_DE_custom.properties`). There you can copy specific strings over from the corresponding language file and customize them to your likings.

VestriaDice has full support for the [MiniMessage Syntax](https://docs.adventure.kyori.net/minimessage.html) so make sure to use it well!

## Debugging

If something goes wrong you can always enable debug messaging at the bottom of the configuration. It will show you a few more messages inside the console helping you to pin down the error. It may be possible that I will request you to turn the debug messaging on before I ask you for your logs inside a GitHub issue.