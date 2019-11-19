<template>
    <div id="app">
      
        <router-view />
    </div>
</template>

<script>
    import BaseHeader from "./components/baser-header";
    export default {
        components: { BaseHeader },
        data() {
            return {};
        }
    };
</script>
