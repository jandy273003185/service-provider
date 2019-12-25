<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  components: {},
  data() {
    return {};
  },
  created() {
   /*  if (sessionStorage.getItem("store")) {
      this.$store.replaceState(
        Object.assign({},this.$store.state,JSON.parse(sessionStorage.getItem("store"))
        )
      );
    }
    window.addEventListener("beforeunload", () => {
      sessionStorage.setItem("store", JSON.stringify(this.$store.state));
    }); */
  }
};
</script>
