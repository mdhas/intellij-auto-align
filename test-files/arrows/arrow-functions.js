      endpoint
        .then((serviceResponse) => {
          let programsList = serviceResponse.data;
          this.set('items', programsList);
        })
        .catch((serviceResponseError) => {
          console.log('error fetching my vue items', serviceResponseError);
        })
        .execute();